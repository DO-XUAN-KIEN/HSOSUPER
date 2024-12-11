//
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.api.objects.InputFile;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
//
//public class BotTelegram extends TelegramLongPollingBot {
//
//    @Override
//    public String getBotUsername() {
//        return "backuphso_bot";
//    }
//
//    @Override
//    public String getBotToken() {
//        return "6994350794:AAHZ3umK12RsC83E56mDHFodQgtG90AUhQM";
//    }
//
//    private void sendFile() {
//        String filePath = "backup.sql";
//        try {
//            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
//
//            File tempFile = File.createTempFile("backup", ".sql");
//            FileOutputStream fos = new FileOutputStream(tempFile);
//            fos.write(fileBytes);
//            fos.close();
//            InputFile inputFile = new InputFile().setMedia(tempFile);
//
//            SendDocument sendDocument = new SendDocument();
//            sendDocument.setChatId("6856452187");
//            sendDocument.setDocument(inputFile);
//            execute(sendDocument);
//        } catch (TelegramApiException e) {
//
//        } catch (IOException ex) {
//            Logger.getLogger(BotTelegram.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void runBot() {
//        try {
//            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//            telegramBotsApi.registerBot(this);
//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    sendFile();
//                }
//            }, 0, 1800000);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        BotTelegram bot = new BotTelegram();
//        bot.runBot();
//    }
//
//    @Override
//    public void onUpdateReceived(Update update) {
//
//    }
//}
