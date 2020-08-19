package com.example.d_fir_login;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CertificateActivity extends AppCompatActivity {
    private static final int STORAGE_CODE = 1000;
    String str, str2, str3;
    TextView txtdisplayname;
    Button btn65b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

        btn65b = findViewById(R.id.btn65b);
        txtdisplayname = findViewById(R.id.txtdisplayname);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        str = extras.getString("age");
        str2 = extras.getString("officerName");
        str3 = extras.getString("caseId");

        txtdisplayname.setText(str2);

        btn65b.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED) {
                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, STORAGE_CODE);
                } else {
                    savePdf();
                }
            }
        });
    }

    private void savePdf() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, 20yy");
        String date = dateFormat.format(calendar.getTime());

        Document mDoc = new Document();

        String mFileName = str3;

        String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/65B_Certificates/" + mFileName + ".pdf";
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/65B_Certificates/");
        if(!dir.exists()){
            dir.mkdir();
        }
        try {

            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));

            mDoc.open();

            Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD | Font.UNDERLINE);
            Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 16);
            Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD | Font.UNDERLINE);
            Font font4 = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
            Font font5 = new Font(Font.FontFamily.TIMES_ROMAN, 19, Font.BOLD);

            Chunk c1 = new Chunk("Addendum", font1);
            //mText.setUnderline(0.5f, -2f);

            Chunk c2 = new Chunk("Certificate under Sec. 65B Evidence Act.", font2);

            //String s1 = "I " + Name + " S/o. Sh " + Father_Name + ", Age " + Age + " R/o. ABC Nagar, is a (profession)..... govt. cyber expert/ Police officer/ cyber cafe operator/ private cyber expert/ an advocate, do hereby solemnly declare and affirms as under that-";
            //Chunk c3 = new Chunk(s1, font2);

            Chunk c3_1 = new Chunk("I  ", font2);
            Chunk c3_2 = new Chunk(str2, font3);
            Chunk c3_3 = new Chunk(", Age  ", font2);
            Chunk c3_4 = new Chunk(str, font3);
            Chunk c3_5 = new Chunk(" is a (profession)..... govt. cyber expert/ Police officer/ cyber cafe operator/ private cyber expert/ an advocate, do hereby solemnly declare and affirms as under that-", font2);

            Chunk c4_1 = new Chunk("   1.   I produced the mobile application ", font2);
            Chunk c4_2 = new Chunk("D-FIR", font4);
            Chunk c4_3 = new Chunk("  output.. (Hard copy/ CD/ DVD/Pen Drive,etc.) of the E- mails/MMS/SMS records/ Whatsapp messenger service records/ call detail records/ Web. Browser records/ CCTV records etc., which represent the link/communication between the alleged offence/ offender and crime/ victim (in criminal cases) or between the parties (in civil cases).", font2);

            Chunk c5_1 = new Chunk("   2.   I further confirm that the computer outputs (E-mails/MMS/SMS records/ Whatsapp messenger service records/ call detail records/ Web. Brower records/ CCTV records etc.) containing the information is/ was produced by mobile application ", font2);
            Chunk c5_2 = new Chunk("D_FIR", font4);
            Chunk c5_3 = new Chunk(" during the period our mobile application is/was used regularly to store and process the information with absolute confidentiality between recorded evidence with discrete hashed value created by the system for security of evidence and the operator of this mobile application.", font2);

            Chunk c6_1 = new Chunk("   3.   I further confirm that I have lawful control over the use of the mobile application which is/was used producing outputs mentioned above.", font2);
            Chunk c6_2 = new Chunk("", font2);

            Chunk c7_1 = new Chunk("   4.   I further confirm that throughout the material part of the said period, the mobile application was operating properly or, if not, then in respect of any period in which it was not operating properly or was out of operation during that part of the period, was not such as to affect the electronic record or the accuracy of its contents.", font2);
            Chunk c7_2 = new Chunk("", font2);

            Chunk c8_1 = new Chunk("   5.   I further confirm that the information contained in the electronic record reproduces or is derived from such information fed into the secured server used by the application in the ordinary course of the said activities.", font2);
            Chunk c8_2 = new Chunk("", font2);

            Chunk c9_1 = new Chunk("   6.   I further confirm that the contents of this affidavit certificate/ affirmation certificate are true to the best of my knowledge and belief.", font2);
            Chunk c9_2 = new Chunk("", font2);

            Chunk c10 = new Chunk(" Dated " + date + ".", font5);

            Chunk c11 = new Chunk(" (Signature) " + str2, font5);


            Paragraph ph = new Paragraph(c1);
            ph.setAlignment(Element.ALIGN_CENTER);

            Paragraph ph2 = new Paragraph(c2);
            ph2.setAlignment(Element.ALIGN_CENTER);

            Paragraph ph3 = new Paragraph();
            ph3.add(c3_1);
            ph3.add(c3_2);
            ph3.add(c3_3);
            ph3.add(c3_4);
            ph3.add(c3_5);


            Paragraph ph4 = new Paragraph();
            ph4.add(c4_1);
            ph4.add(c4_2);
            ph4.add(c4_3);

            Paragraph ph5 = new Paragraph();
            ph5.add(c5_1);
            ph5.add(c5_2);
            ph5.add(c5_3);

            Paragraph ph6 = new Paragraph();
            ph6.add(c6_1);
            ph6.add(c6_2);

            Paragraph ph7 = new Paragraph();
            ph7.add(c7_1);
            ph7.add(c7_2);

            Paragraph ph8 = new Paragraph();
            ph8.add(c8_1);
            ph8.add(c8_2);

            Paragraph ph9 = new Paragraph();
            ph9.add(c9_1);
            ph9.add(c9_2);

            Paragraph ph10 = new Paragraph(c10);

            Paragraph ph11 = new Paragraph(c11);
            ph11.setAlignment(Element.ALIGN_RIGHT);

            mDoc.add(ph);
            mDoc.add(new Paragraph("  "));
            mDoc.add(ph2);
            mDoc.add(new Paragraph("  "));
            mDoc.add(ph3);
            mDoc.add(new Paragraph("  "));
            mDoc.add(ph4);
            mDoc.add(new Paragraph("  "));
            mDoc.add(ph5);
            mDoc.add(new Paragraph("  "));
            mDoc.add(ph6);
            mDoc.add(new Paragraph("  "));
            mDoc.add(ph7);
            mDoc.add(new Paragraph("  "));
            mDoc.add(ph8);
            mDoc.add(new Paragraph("  "));
            mDoc.add(ph9);
            mDoc.add(new Paragraph("  "));
            mDoc.add(new Paragraph("  "));
            mDoc.add(new Paragraph("  "));
            mDoc.add(ph10);
            mDoc.add(new Paragraph("  "));
            mDoc.add(ph11);

            mDoc.close();

            Toast.makeText(this, mFileName + ".pdf is saved to\n" + mFilePath, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                savePdf();
            } else {
                Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}