package hr.math.anatravica;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    long startDownloadTime = 0;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final EditText etUrl = findViewById(R.id.urlId);
        etUrl.setText("https://static.igre123.net/slike/111000-39171/pingvini.jpg");

        //https://upload.wikimedia.org/wikipedia/bs/thumb/f/f4/The_Scream.jpg/250px-The_Scream.jpg
        //http://opusteno.rs/slike/desktop-pozadine/21289/slike-lava-desktop-t29.jpg
        // https://ocdn.eu/pulscms-transforms/1/OakktkqTURBXy9iYzgwZGYzZWUyZGY3ZTBiYzBiNDgxZmFlZjRmNzI0MS5qcGVnk5UCzQMUAMLDlQIAzQL4wsOVB9kyL3B1bHNjbXMvTURBXy8xZDc0Y2I0MTcwNTk1MDQzNjYyOWNhYmQ2MDZmNTBmNi5wbmcHwgA

        Button buttonDownload = findViewById(R.id.downloadButton);
        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = etUrl.getText().toString();
                skini(url);
            }
        });


        Button database = findViewById(R.id.database);
        database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DatabaseActivity.class);
                startActivity(intent);
            }
        });

    }


    private InputStream OpenHttpConnection(String urlString)
            throws IOException
    {
        InputStream in = null;
        int response = -1;

        java.net.URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }
        catch (Exception ex)
        {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }


    private void skini(String url)
    {

        new DownloadImageTask().execute(url);

    }

    private Bitmap DownloadImage(String URL)
    {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
            Log.d("NetworkingActivity", e1.getLocalizedMessage());
        }
        return bitmap;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            startDownloadTime = System.currentTimeMillis();
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... urls) {
            return DownloadImage(urls[0]);
        }

        protected void onPostExecute(Bitmap result) {
            ImageView img = findViewById(R.id.mojaSlika);
            img.setImageBitmap(result);
            long currentTime = System.currentTimeMillis();
            displayNotification((currentTime - startDownloadTime));
        }
    }

    private void displayNotification(long time)
    {
        //---PendingIntent to launch activity if the user selects
        // this notification---
        Intent i = new Intent(this, Main4Activity.class);

        i.putExtra("notificationID", 1);
        i.putExtra("url", url);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, i, 0);

        long[] vibrate = new long[] { 100, 250, 100, 500};

//za sve verzije
        NotificationManager nm = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);


        final NotificationCompat.Builder notif = new NotificationCompat.Builder(this, "12")

                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(vibrate)
                .setSound(null)
                .setChannelId("12")
                .setContentTitle("Slika je skinuta")
                .setContentText("Vrijeme skidanja (milisekunde): " + time)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

// za sve verzije

        nm.notify(1, notif.build());
    }




}
