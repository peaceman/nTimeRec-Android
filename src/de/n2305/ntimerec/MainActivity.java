package de.n2305.ntimerec;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	if (scanResult != null) {
    		try {
    			JSONTokener jsonTokener = new JSONTokener(scanResult.getContents());
    			JSONObject jsonValue = new JSONObject(jsonTokener);
    			
        		TextView resultView = (TextView)findViewById(R.id.scan_result);
        		activateDevice(jsonValue);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "invalid activation qr code", 10).show();
			}
    	}
    }
    
    public void activateDevice(JSONObject jsonValue) throws JSONException
    {
    	long deviceId = jsonValue.getLong("device_id");
    	long userId = jsonValue.getLong("user_id");
    	String secret = jsonValue.getString("secret");
    	
    	_saveDeviceDataToPreferences(deviceId, userId, secret);
    }
    
    /**
     * Saves the given device data into a private SharedPreferences-Storage
     * 
     * @param deviceId
     * @param userId
     * @param secret
     */
    protected void _saveDeviceDataToPreferences(long deviceId, long userId, String secret)
    {
    	SharedPreferences settings = getPreferences(MODE_PRIVATE);
    	SharedPreferences.Editor settingsEditor = settings.edit();
    	
    	settingsEditor.putLong("device_id", deviceId);
    	settingsEditor.putLong("user_id", userId);
    	settingsEditor.putString("secret", secret);
    	
    	settingsEditor.apply();
    }
    
    public void sendMessage(View view) {
    	switch (view.getId()) {
    	case R.id.trigger_scan:
    		IntentIntegrator intentIntegrator = new IntentIntegrator(this);
    		intentIntegrator.initiateScan();
    		break;
    	}
    }
}
