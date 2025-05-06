package com.feelody.feelody_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feelody.feelody_backend.dto.FeedbackRespDto;
import com.feelody.feelody_backend.entity.Feedback;
import com.feelody.feelody_backend.repository.FeedbackRepository;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Gemini API를 사용하여 오디오 파일을 분석하고 피드백을 생성하는 서비스입니다.
 * <p>
 * 1. 오디오 파일을 업로드할 곳의 URL을 요청합니다. 2. 오디오 파일을 업로드하고 해당 파일이 저장된 URL을 가져옵니다. 3. 업로드된 파일이 저장된 URL을 바탕으로 Gemini API에 요청을
 * 보냅니다.
 */


@Slf4j
@Service
@AllArgsConstructor
public class FeedbackService {

    private FeedbackRepository feedbackRepository;
    private final String GOOGLE_API_KEY = System.getenv("GOOGLE_API_KEY");

    public FeedbackRespDto geminiAnalyze(MultipartFile audio) throws IOException {
        final String command = """
                {
                  "contents": [{
                    "parts": [
                      {"text": "이 오디오 클립은 음악 연주입니다. 이 연주에 대해 다음과 같은 기준으로 평가해주세요: ① 정확도, ② 표현력, ③ 리듬 감각, ④ 음정. 총 점수(100점 만점)만 정수 형태로 제공해주세요."},
                      {"file_data": {"mime_type": "%s", "file_uri": "%s"}}
                    ]
                  }]
                }
                """;

        String mimeType = audio.getContentType() != null ? audio.getContentType() : null;
        String numBytes = String.valueOf(audio.getSize());

        String uploadUrl = getUploadUrl(numBytes, audio.getContentType());
        String fileUri = getUploadedFileURl(uploadUrl, numBytes, audio);

        String prompt = String.format(command, mimeType, fileUri);

        Feedback feedback = Feedback.builder()
                .title("제목")
                .artist("아티스트")
                .score(Integer.parseInt(doGemini(prompt)))
                .build();

        Feedback saved = feedbackRepository.save(feedback);

        return FeedbackRespDto.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .artist(saved.getArtist())
                .score(saved.getScore())
                .createdAt(saved.getCreatedAt().toLocalDate())
                .build();
    }

    private String doGemini(String prompt) throws IOException {
        String analyzeUrl =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
                        + GOOGLE_API_KEY;
        HttpURLConnection contentConn = (HttpURLConnection) URI.create(analyzeUrl).toURL().openConnection();
        contentConn.setRequestMethod("POST");
        contentConn.setRequestProperty("Content-Type", "application/json");
        contentConn.setDoOutput(true);

        sendReqeustToGoogle(contentConn, prompt);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(contentConn.getInputStream());
        log.info("Response: {}", root.toPrettyString());
        return root.path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();
    }

    private String getUploadUrl(String numBytes, String mimeType) throws IOException {
        String initialRequestUrl =
                "https://generativelanguage.googleapis.com/upload/v1beta/files?key=" + GOOGLE_API_KEY;
        HttpURLConnection startConn = (HttpURLConnection) URI.create(initialRequestUrl).toURL().openConnection();
        startConn.setRequestMethod("POST");
        startConn.setRequestProperty("X-Goog-Upload-Protocol", "resumable");
        startConn.setRequestProperty("X-Goog-Upload-Command", "start");
        startConn.setRequestProperty("X-Goog-Upload-Header-Content-Length", numBytes);
        startConn.setRequestProperty("X-Goog-Upload-Header-Content-Type", mimeType);
        startConn.setRequestProperty("Content-Type", "application/json");
        startConn.setDoOutput(true);

        String json = "{\"file\":{\"display_name\":\"AUDIO\"}}";
        sendReqeustToGoogle(startConn, json);

        Map<String, List<String>> headers = startConn.getHeaderFields();
        return headers.entrySet().stream()
                .filter(e -> "X-Goog-Upload-URL".equalsIgnoreCase(e.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(List::stream)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Upload URL not found"));
    }

    private static void sendReqeustToGoogle(HttpURLConnection startConn, String message) throws IOException {
        try (OutputStream os = startConn.getOutputStream()) {
            os.write(message.getBytes());
        }
    }

    private String getUploadedFileURl(String fileUploadPathUrl, String numBytes, MultipartFile audio)
            throws IOException {
        HttpURLConnection uploadConn = (HttpURLConnection) URI.create(fileUploadPathUrl).toURL().openConnection();
        uploadConn.setRequestMethod("POST");
        uploadConn.setRequestProperty("Content-Length", numBytes);
        uploadConn.setRequestProperty("X-Goog-Upload-Offset", "0");
        uploadConn.setRequestProperty("X-Goog-Upload-Command", "upload, finalize");
        uploadConn.setDoOutput(true);

        try (OutputStream os = uploadConn.getOutputStream(); InputStream is = audio.getInputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode fileJson = mapper.readTree(uploadConn.getInputStream());

        return fileJson.path("file").path("uri").asText();
    }
}
