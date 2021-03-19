
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Painter {

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private void write(String path, String content){
        try {
            FileWriter fw = new FileWriter(new File(path));
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.flush();
            bw.close();
        } catch (Exception e) {
        }
    }

    public void run(String path) throws IOException, ParseException {
        JSONParser parse = new JSONParser();
        File file = new File(path);

        String defaultString = "{\n" +
                "\"pathData\": \"RRRRRRRRRR\", \n" +
                "\"settings\":\n" +
                "{\n" +
                "\"version\": 2, \n" +
                "\"artist\": \"\", \n" +
                "\"specialArtistType\": \"None\", \n" +
                "\"artistPermission\": \"\", \n" +
                "\"song\": \"\", \n" +
                "\"author\": \"만든이\", \n" +
                "\"separateCountdownTime\": \"Enabled\", \n" +
                "\"previewImage\": \"\", \n" +
                "\"previewIcon\": \"\", \n" +
                "\"previewIconColor\": \"003f52\", \n" +
                "\"previewSongStart\": 0, \n" +
                "\"previewSongDuration\": 10, \n" +
                "\"seizureWarning\": \"Disabled\", \n" +
                "\"levelDesc\": \"레벨에 대해 말해보세요!\", \n" +
                "\"levelTags\": \"\", \n" +
                "\"artistLinks\": \"\", \n" +
                "\"difficulty\": 1,\n" +
                "\"songFilename\": \"\", \n" +
                "\"bpm\": 100, \n" +
                "\"volume\": 100, \n" +
                "\"offset\": 0, \n" +
                "\"pitch\": 100, \n" +
                "\"hitsound\": \"Kick\", \n" +
                "\"hitsoundVolume\": 100, \n" +
                "\"countdownTicks\": 4,\n" +
                "\"trackColorType\": \"Single\", \n" +
                "\"trackColor\": \"debb7b\", \n" +
                "\"secondaryTrackColor\": \"ffffff\", \n" +
                "\"trackColorAnimDuration\": 2, \n" +
                "\"trackColorPulse\": \"None\", \n" +
                "\"trackPulseLength\": 10, \n" +
                "\"trackStyle\": \"Standard\", \n" +
                "\"trackAnimation\": \"None\", \n" +
                "\"beatsAhead\": 3, \n" +
                "\"trackDisappearAnimation\": \"None\", \n" +
                "\"beatsBehind\": 4,\n" +
                "\"backgroundColor\": \"000000\", \n" +
                "\"bgImage\": \"\", \n" +
                "\"bgImageColor\": \"ffffff\", \n" +
                "\"parallax\": [100, 100], \n" +
                "\"bgDisplayMode\": \"FitToScreen\", \n" +
                "\"lockRot\": \"Disabled\", \n" +
                "\"loopBG\": \"Disabled\", \n" +
                "\"unscaledSize\": 100,\n" +
                "\"relativeTo\": \"Global\", \n" +
                "\"position\": [-50, -50], \n" +
                "\"rotation\": 0, \n" +
                "\"zoom\": 2000,\n" +
                "\"bgVideo\": \"\", \n" +
                "\"loopVideo\": \"Disabled\", \n" +
                "\"vidOffset\": 0, \n" +
                "\"floorIconOutlines\": \"Disabled\", \n" +
                "\"stickToFloors\": \"Disabled\", \n" +
                "\"planetEase\": \"Linear\", \n" +
                "\"planetEaseParts\": 1\n" +
                "},\n" +
                "\"actions\":\n" +
                "[\n" +
                "]\n" +
                "}\n";

        JSONObject object = (JSONObject)parse.parse(defaultString);
        JSONArray actions = ((JSONArray)object.get("actions"));

        String tileColor = "{ \"floor\": 0, \"eventType\": \"ColorTrack\", \"trackColorType\": \"Single\", \"trackColor\": \"ffffff\", \"secondaryTrackColor\": \"ffffff\", \"trackColorAnimDuration\": 2, \"trackColorPulse\": \"None\", \"trackPulseLength\": 10, \"trackStyle\": \"Standard\" }";
        String enter = "{ \"floor\": 0, \"eventType\": \"PositionTrack\", \"positionOffset\": [-100, -1], \"editorOnly\": \"Disabled\" }";

        StringBuilder path2 = new StringBuilder();
        int n = 0;

        BufferedImage bufferedImage = ImageIO.read(file);
        bufferedImage = resizeImage(bufferedImage,100,100);

        for(int y=0;y<100;y++) {

            for(int x=0;x<100;x++) {
                path2.append("R");
                int clr = bufferedImage.getRGB(x,y);
                String hex = Integer.toHexString(clr).substring(2);
                JSONObject tile = (JSONObject)parse.parse(tileColor);
                tile.put("floor",n);
                tile.put("trackColor",hex);
                actions.add(tile);
                n++;
            }

            JSONObject ent = (JSONObject)parse.parse(enter);
            ent.put("floor",100*y);
            actions.add(ent);
        }

        object.put("actions",actions);
        object.put("pathData", path2.toString());

        String desktop = System.getProperty("user.home");
        write(desktop+"\\Desktop\\level.adofai",object.toString());
        System.out.println("제작 끝");
    }

}
