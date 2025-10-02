//package stark.coderaider.titan.gate.core.services;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import stark.coderaider.titan.gate.api.dtos.responses.AuthResult;
//import stark.coderaider.titan.gate.api.dtos.responses.LoginResponse;
//import stark.coderaider.titan.gate.core.utils.JwtUtil;
//
//import java.io.IOException;
//
//@Service
//public class OAuth2Service
//{
//
//    @Value("${oauth2.github.client-id:}")
//    private String githubClientId;
//
//    @Value("${oauth2.github.client-secret:}")
//    private String githubClientSecret;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    private final OkHttpClient client = new OkHttpClient();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public AuthResult<LoginResponse> handleGitHubLogin(String code, String redirectUri)
//    {
//        try
//        {
//            // 用授权码换取access_token
//            String accessToken = exchangeCodeForAccessToken(code, redirectUri);
//            if (accessToken == null)
//            {
//                return AuthResult.error("获取GitHub访问令牌失败");
//            }
//
//            // 获取用户信息
//            JsonNode userInfo = getGitHubUserInfo(accessToken);
//            if (userInfo == null)
//            {
//                return AuthResult.error("获取GitHub用户信息失败");
//            }
//
//            String githubId = userInfo.get("id").asText();
//            String username = userInfo.get("login").asText();
//            String email = userInfo.has("email") ? userInfo.get("email").asText() : null;
//
//            // 在实际应用中，这里应该检查用户是否已存在，如果不存在则创建新用户
//            // 这里简化处理，直接生成token
//
//            String token = jwtUtil.generateToken(username);
//
//            LoginResponse response = new LoginResponse();
//            response.setToken(token);
//            response.setUsername(username);
//            response.setEmail(email);
//            // 在实际应用中，这里应该设置真实的用户ID
//            response.setUserId(1L);
//
//            return AuthResult.success(response);
//        }
//        catch (Exception e)
//        {
//            return AuthResult.error("GitHub登录失败: " + e.getMessage());
//        }
//    }
//
//    private String exchangeCodeForAccessToken(String code, String redirectUri) throws IOException
//    {
//        String url = "https://github.com/login/oauth/access_token";
//
//        MediaType mediaType = MediaType.parse("application/json");
//        String json = String.format("{\"client_id\":\"%s\",\"client_secret\":\"%s\",\"code\":\"%s\",\"redirect_uri\":\"%s\"}",
//            githubClientId, githubClientSecret, code, redirectUri);
//        RequestBody body = RequestBody.create(json, mediaType);
//
//        Request request = new Request.Builder()
//            .url(url)
//            .post(body)
//            .addHeader("Accept", "application/json")
//            .build();
//
//        try (Response response = client.newCall(request).execute())
//        {
//            if (!response.isSuccessful())
//            {
//                return null;
//            }
//
//            String responseBody = response.body().string();
//            JsonNode jsonNode = objectMapper.readTree(responseBody);
//            return jsonNode.has("access_token") ? jsonNode.get("access_token").asText() : null;
//        }
//    }
//
//    private JsonNode getGitHubUserInfo(String accessToken) throws IOException
//    {
//        String url = "https://api.github.com/user";
//
//        Request request = new Request.Builder()
//            .url(url)
//            .addHeader("Authorization", "token " + accessToken)
//            .addHeader("Accept", "application/json")
//            .build();
//
//        try (Response response = client.newCall(request).execute())
//        {
//            if (!response.isSuccessful())
//            {
//                return null;
//            }
//
//            String responseBody = response.body().string();
//            return objectMapper.readTree(responseBody);
//        }
//    }
//}