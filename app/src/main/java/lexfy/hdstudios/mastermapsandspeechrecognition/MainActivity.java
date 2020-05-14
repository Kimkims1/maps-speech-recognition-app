package lexfy.hdstudios.mastermapsandspeechrecognition;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import lexfy.hdstudios.mastermapsandspeechrecognition.Model.CountryDataSource;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SPEAK_REQUEST = 10;

    private TextView sampleText;

    private Button btnTalk;

    public static CountryDataSource countryDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sampleText = findViewById(R.id.sampleTv);
        btnTalk = findViewById(R.id.btnTalk);

        btnTalk.setOnClickListener(MainActivity.this);

        Hashtable<String, String> countriesAndMessages = new Hashtable<>();
        countriesAndMessages.put("Canada", "Welcome to Canada");
        countriesAndMessages.put("France", "Welcome to France. Happy visiting");
        countriesAndMessages.put("Brazil", "Welcome to Brazil. Happy visiting");
        countriesAndMessages.put("USA", "Welcome to USA. Happy visiting");
        countriesAndMessages.put("UK", "Welcome to UK. Happy visiting");
        countriesAndMessages.put("Spain", "Welcome to Spain. Happy visiting");
        countriesAndMessages.put("Kenya", "Welcome to Kenya. Happy visiting");

        countryDataSource = new CountryDataSource();


        PackageManager packageManager = this.getPackageManager();

        List<ResolveInfo> listInfo = packageManager.queryIntentServices(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0
        );

        if (listInfo.size() > 0) {

            Toast.makeText(this, "Your device supports Speech Recognition", Toast.LENGTH_SHORT).show();

            listenToTheUserVoice();
        } else {
            Toast.makeText(this, "Your device does not supports Speech Recognition", Toast.LENGTH_SHORT).show();

        }
    }

    private void listenToTheUserVoice() {
        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Talk to me...!");
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        startActivityForResult(voiceIntent, SPEAK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEAK_REQUEST && resultCode == RESULT_OK) {

            assert data != null;
            ArrayList<String> voiceWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            float[] confidLevels = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);

            /*int index = 0;
            for (String userWord : voiceWords) {
                if (confidLevels != null && index < confidLevels.length) {
                    sampleText.setText(userWord + " - " + confidLevels[index]);
                }
            }*/

            String countryMatchedWithUserWord = countryDataSource.matchWithMinimumConfidenceLevelOfUserWords(voiceWords, confidLevels);

            Intent myMapActivity = new Intent(MainActivity.this, MapsActivity.class);
            myMapActivity.putExtra(CountryDataSource.COUNTRY_KEY, countryMatchedWithUserWord);
            startActivity(myMapActivity);

        }
    }

    @Override
    public void onClick(View v) {

        listenToTheUserVoice();

    }
}
