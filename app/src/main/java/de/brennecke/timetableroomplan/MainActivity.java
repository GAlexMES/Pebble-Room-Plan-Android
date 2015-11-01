package de.brennecke.timetableroomplan;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.regex.Pattern;

import de.brennecke.timetableroomplan.model.Exchange;
import de.brennecke.timetableroomplan.model.TTBL;

public class MainActivity extends Activity {

    private static final int REQUEST_PATH = 1;
    String curFileName, curFilePath;
    EditText edittext;
    Button showOnClock, loadFileButton;

    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //addListenerOnButton();
        edittext = (EditText)findViewById(R.id.editText);
        loadFileButton = (Button)findViewById(R.id.loadFileButton);
        loadFileButton.setEnabled(false);

        Intent alarmIntent = new Intent(MainActivity.this, UpdateReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isConnected = PebbleKit.isWatchConnected(this);
        Toast.makeText(this, "Pebble " + (isConnected ? "is" : "is not") + " connected!", Toast.LENGTH_LONG).show();

    }

    public void getfile(View view){
        loadFileButton.setEnabled(false);
        Intent intent1 = new Intent(this, FileChooser.class);
        startActivityForResult(intent1, REQUEST_PATH);
    }

    public void loadFile(View view){
        try {
            TTBLParser ttblParser = new TTBLParser(curFilePath , curFileName, getApplicationContext());
            TTBL ttbl = ttblParser.getTTBL();
            SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(MainActivity.this);
            sqLiteSourceAdapter.addTTBL(ttbl);
            Exchange.getInstance().setTTBL(ttbl);
            Exchange.getInstance().setPendingIntent(pendingIntent);
            Exchange.getInstance().startAlarm(getApplicationContext());
        }
        catch(IOException ioe){
            Toast.makeText(this, "Could not found the file!", Toast.LENGTH_LONG).show();
        }
        catch (XmlPullParserException xppe){
            xppe.printStackTrace();
        }
        }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
        if (requestCode == REQUEST_PATH){
            if (resultCode == RESULT_OK) {
                curFilePath = data.getStringExtra("GetPath");
                curFileName = data.getStringExtra("GetFileName");
                edittext.setText(curFileName);
                String splittedFileName[] = curFileName.split(Pattern.quote("."));
                if(splittedFileName[splittedFileName.length-1].equals("ttbl")){
                    loadFileButton.setEnabled(true);
                }
                else{
                    Toast.makeText(this, "Please select a valid .tbbl file!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
