package com.example.android.pdftest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText contentet, filenameet;

    String basePath = Environment.getExternalStorageDirectory() + "/Naaniz/";
    String filename = "test.pdf";

    LinearLayout sharell;
    TextView savedtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentet = findViewById(R.id.content_et);
        filenameet = findViewById(R.id.filename_et);

        sharell = findViewById(R.id.share_ll);
        sharell.setVisibility(View.GONE);

        savedtv = findViewById(R.id.saved_tv);

        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filenameet.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this, "Enter file name", Toast.LENGTH_SHORT).show();
                else createPdf();
            }
        });

        findViewById(R.id.share_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Uri uri = Uri.fromFile(new File(basePath + filename));
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_STREAM, uri);

                startActivity(Intent.createChooser(intent, "Select app to share"));
            }
        });

    }
    /*private void createPdf(String content, String filename){

        if(content.isEmpty()) content = "No content available";
        if(!filename.contains(".")) filename += ".pdf";
        this.filename = filename;

        File baseFolder = new File(basePath);
        verifyStoragePermissions(this);
        if(!baseFolder.exists()) {
            baseFolder.mkdirs();
            Log.v("pdfcreated", "Base folder created");
        }
        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(300, 600, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint headerPaint = new Paint();
        headerPaint.setColor(Color.DKGRAY);
        headerPaint.setTextSize(48);
        canvas.drawText("Naaniz", 50, 50, headerPaint);

        Paint bodyPaint = new Paint();
        bodyPaint.setColor(Color.GRAY);
        bodyPaint.setTextSize(16);
        canvas.drawText(content, 10, 100, bodyPaint);

        // finish the page
        document.finishPage(page);

        // write the document content
        File file = new File(baseFolder.getAbsoluteFile()+"/"+filename);
        try {
            if(!file.exists()) file.createNewFile();
            document.writeTo(new FileOutputStream(file));
            savedtv.setText("PDF file saved at location : " + file.getAbsolutePath());
            sharell.setVisibility(View.VISIBLE);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(),
                    Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }*/

    private void createPdf(){
        Document document = new Document();
        String content = contentet.getText().toString();
        String extraContent = "this is some extra content lorem ipsum bla bla";
        String header = "Naaniz - Recipe";
        String basePath = Environment.getExternalStorageDirectory() + "/Naaniz/";
        String filename = "tests.pdf";

        Paragraph testPara = new Paragraph(header, FontFactory.getFont(FontFactory.COURIER_BOLD,20, Font.BOLD, BaseColor.BLACK));
        testPara.setAlignment(Element.ALIGN_CENTER);
        Paragraph prescription = new Paragraph(content,FontFactory.getFont(FontFactory.COURIER,15,Font.BOLD, BaseColor.BLACK));
        Paragraph datapresc = new Paragraph(extraContent,FontFactory.getFont(FontFactory.COURIER,10,Font.NORMAL, BaseColor.BLACK));

        File baseFolder = new File(basePath);
        if(!baseFolder.exists()) {
            baseFolder.mkdirs();
            Log.v("pdfcreated", "Base folder created");
        }

        try{
            PdfWriter.getInstance(document, new FileOutputStream(basePath+filename));
            document.open();
            document.addAuthor("orashar");
            document.add(testPara);
            document.add(prescription);
            document.add(datapresc);
            document.close();
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1
            );
            //createPdf(contentet.getText().toString(), filenameet.getText().toString());
        }

    }

}
