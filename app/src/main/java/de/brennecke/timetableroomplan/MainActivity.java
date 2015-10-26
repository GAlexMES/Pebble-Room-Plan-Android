package de.brennecke.timetableroomplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.getpebble.android.kit.Constants;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import de.brennecke.timetableroomplan.model.Exchange;
import de.brennecke.timetableroomplan.model.TTBL;

public class MainActivity extends Activity {

    private static final int REQUEST_PATH = 1;
    String curFileName, curFilePath;
    EditText edittext;
    Button showOnClock, loadFileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //addListenerOnButton();
        edittext = (EditText)findViewById(R.id.editText);
        loadFileButton = (Button)findViewById(R.id.loadFileButton);
        loadFileButton.setEnabled(false);
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
            Exchange.getInstance().setTTBL(ttbl);
            Exchange.getInstance().startService(getApplicationContext());
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
