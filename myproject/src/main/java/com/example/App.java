package com.example;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

public class App extends TelegramLongPollingBot {
    int p = 0 ;
    @Override
    public String getBotUsername() {
        return "bot-user-name";//your bot user name
    }

    @Override
    public String getBotToken() {
        return "bot-token .......................";//your bot token get from bot father
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            String my_chat_id = "developer-chat-id";
            long chatId = update.getMessage().getChatId();
            String user = update.getMessage().getFrom().getFirstName();
//            String chat_user = String.valueOf(chatId);
            String message_info = "chat id = " + chatId + "user_name = " + user;

            String url0 = "https://api.telegram.org/bot6132864830:AAHRB7WcID0wzRqlloVnwlQqlATWpI-QGMA/sendMessage";
            try {
                Connection.Response response = Jsoup.connect(url0).ignoreContentType(true).data("chat_id", my_chat_id).data("text", message_info).method(Connection.Method.POST).execute();
            } catch (Exception a) {
                System.out.println(a);
            }

            SendMessage message = new SendMessage();
            message.setChatId(chatId);

            if (messageText.equals("/start")) {
                String first_message = "Hello " + user + " !\nEnter or paste instagram reel url ";
                message.setText(first_message);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else if(messageText.contains("https://www.instagram.com")) {
                System.out.println("path number - "+p);p++;
                String first_message1 = "Wait a moment; your content is downloading.";
                message.setText(first_message1);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                
////////////////////////////////////////////////main function
                String path2 = "";
                String ins_url = messageText;
                Fetch_url obj1 = new Fetch_url();
                String video_url = obj1.fetch_1(ins_url);

                if(video_url.isEmpty()){
                    String first_message2 = "url incorrect ....";
                    message.setText(first_message2);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                else {
//                  System.out.println("main class " + video_url);
                    Download_class obj2 = new Download_class();
                    path2 = obj2.download(video_url,p);
                }
//                System.out.println(path2);
                String path21 = path2;
                SendVideo video = new SendVideo();
                video.setChatId(chatId);
                video.setVideo(new InputFile(new File(path2)));
                p++;
                try {
                    execute(video);
                } catch (Exception a) {
                    System.out.println(a);
                }
                String first_message2 = "Download another ..........";
                message.setText(first_message2);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }
            else{
                String first_message2 = "Provide correct url";
                message.setText(first_message2);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new App());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

class Fetch_url {
    public String fetch_1(String ins_url) {
        String href = "";
        String url = "https://v3.igdownloader.app/api/ajaxSearch";
//        String found_url = "";

        try {
            Map<String, String> post_data = Map.of(
                    "t", "media",
                    "lang", "en",
                    "q", ins_url,
                    "recaptchaToken", ""
            );
            System.out.println(post_data);

            String request_body = post_data.entrySet()
                    .stream()
                    .map(entry -> {
                        try {
                            return URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
                        } catch (Exception e) {
                            System.out.println("Exception in encoding - " + e);
                            return "";
                        }
                    })
                    .collect(Collectors.joining("&"));

//System.out.println(request_body);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .POST(HttpRequest.BodyPublishers.ofString(request_body))
                    .header("Sec-Ch-Ua", "\"Chromium\";v=\"123\", \"Not:A-Brand\";v=\"8\"")
                    .header("Accept", "*/*")
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .header("Sec-Ch-Ua-Mobile", "?0")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.6312.58 Safari/537.36")
                    .header("Sec-Ch-Ua-Platform", "\"Linux\"")
                    .header("Origin", "https://igdownloader.app")
                    .header("Sec-Fetch-Site", "same-site")
                    .header("Sec-Fetch-Mode", "cors")
                    .header("Sec-Fetch-Dest", "empty")
                    .header("Referer", "https://igdownloader.app/")
                    .header("Accept-Language", "en-GB,en;q=0.9")
                    .header("Priority", "u=1, i")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String response_out = response.body();
//            System.out.println(response_out);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response_out);
            String data = jsonNode.path("data").asText();

//            System.out.println("data of json -" + data);

            Document doc = Jsoup.parse(data);
            Elements element = doc.select("[href]");
//            System.out.println(element);

            href = element.attr("href");
//            System.out.println("Link Href: " + href);

        } catch (Exception e) {
            System.out.println(e);
        }
        return href;
    }
}

class Download_class {

    public String download(String a_url,int callCount) {
        String video_path1= "";
        video_path1= "ins"+callCount+".mp4";
//      System.out.println(video_path1);
        Path video_path = Paths.get(video_path1);

        try {

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(a_url)).GET().build();
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
//                System.out.println(response.body());

            if (response.statusCode() == 200) {
                Files.write(video_path, response.body());
            } else {
                System.out.println("video not downloaded");
            }
            callCount++;
//            System.out.println(callCount);

        } catch (Exception e) {
            System.out.println(e);
        }
        return video_path1;

    }

}
