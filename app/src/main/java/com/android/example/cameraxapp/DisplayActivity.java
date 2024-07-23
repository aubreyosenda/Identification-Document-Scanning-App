package com.android.example.cameraxapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.atomic.AtomicReference;
import com.hbb20.CountryCodePicker;

public class DisplayActivity extends AppCompatActivity {

    private TextView textNameView, textDocNoView, textPhoneNoView, selectedDocumentView, selectedCountryView;
    private static final int REQUEST_PHONE_VERIFICATION = 1;

    private Button buttonRetake;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

//        Get the intent
        AtomicReference<Intent> intent = new AtomicReference<>(getIntent());

      // Initialize views

        progressBar = findViewById(R.id.Progressbar);
        progressBar.setVisibility(View.GONE);

        textNameView = findViewById(R.id.name_text);
        textDocNoView = findViewById(R.id.doc_no_text);
        textPhoneNoView = findViewById(R.id.phone_number_text);
        selectedDocumentView = findViewById(R.id.selected_document_text);
        selectedCountryView = findViewById(R.id.selected_country_text);

        Button buttonRetake = findViewById(R.id.button_retake);
        Button buttonCopy = findViewById(R.id.button_copy);


        String extractedText = intent.get().getStringExtra("extractedText"); // Get the extracted text passed from CameraActivity
        String documentType = intent.get().getStringExtra("selectedDocument");
        String country = intent.get().getStringExtra("selectedCountry");
//        String phoneNumber = intent.get().getStringExtra("phoneNo");

        selectedDocumentView.setText(documentType);
        selectedCountryView.setText(country);
//        textPhoneNoView.setText(phoneNumber);


        if (extractedText != null) {
            String[] lines = extractedText.split("\n");  // Create a new array to store modified strings

            // remove all white spaces
            for (int i = 0; i < lines.length; i++) {
                lines[i] = lines[i].replaceAll("\\s", "");
            }

            if ("ID Card".equals(documentType)){
                getIDCardDetails(lines);
            } else if ("Passport".equals(documentType)) {
                getPassportDetails(lines);
            } else if ("Driving License".equals(documentType)) {
                Toast.makeText(DisplayActivity.this, "Selected Doc is DL ", Toast.LENGTH_SHORT).show();
            } else {
                intent.set(new Intent(DisplayActivity.this, GetStartedActivity.class));
                startActivity(intent.get());
                finish();
            }

        } else {
            Toast.makeText(this, "No text found. Please Retake image", Toast.LENGTH_SHORT).show();
            // Handle case where no text is passed
            finish();
        }


        // Set onClickListener for Retake button
        buttonRetake.setOnClickListener(v -> {
            intent.set(new Intent(DisplayActivity.this, GetStartedActivity.class));
            startActivity(intent.get());
            finish();
        });
    }

    private void getPassportDetails(String[] text) {
//        Get Passport name
        String nameField = text[text.length - 2].substring(5); //Get second last line
        String[] nameArr = nameField.split("<");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < nameArr.length; i++) {
            if (nameArr[i].length() > 2) {
                sb.append(nameArr[i]).append(" ");
            }
            if (i == 3) {
                break;
            }
        }

        String fullPassportName = sb.toString().trim();
        textNameView.setText(fullPassportName);
        sb.setLength(0);

        // Get the Passport number from the last Line
        String passNoField = text[text.length - 1].substring(0,8);
        textDocNoView.setText(passNoField);

//        Toast.makeText(DisplayActivity.this, "Finished", Toast.LENGTH_SHORT).show();

    }

    private void getIDCardDetails(String[] lines) {
        textNameView.setText(replaceSpecial(lines[lines.length - 1])); // Last line in the scan
        String iDNoLine = lines[lines.length - 2];
        String reversedText = reverseString(iDNoLine);
        String reversedIdNo = reversedText.substring(4, 13);
        String dummyIdNo = reverseString(reversedIdNo);

        // Check if dummyIdNo starts with '0' or 'O'
        if (dummyIdNo.startsWith("0") || dummyIdNo.startsWith("O") || dummyIdNo.startsWith("o")) {
            // Discard the first character
            textDocNoView.setText(dummyIdNo.substring(1));
        } else{
            textDocNoView.setText(dummyIdNo);
        }
    }

    public static String reverseString(String text) {
        return new StringBuilder(text).reverse().toString();
    }

    public static String replaceSpecial(String newText) {
        return newText.replaceAll("<", " ");
    }

    private void copyToClipboard(String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }
}
