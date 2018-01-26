package hr.math.anatravica;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DatabaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);


        final mojaBaza mojaBazaVar = new mojaBaza(this);

        Button insert = findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mojaBazaVar.open();
                mojaBazaVar.insertSlika("Guernica", "Picasso");
                mojaBazaVar.insertSlika(" Dama s hermelinom", "Leonardo da Vinci");
                mojaBazaVar.insertSlika("Gundulićev san", "Vlaho Bukovac");
                mojaBazaVar.insertSlika("Povratak razmetnoga sina", "Rembrandt");
                mojaBazaVar.insertSlika("Zena koja place", "Picasso");

                mojaBazaVar.insertPeriod("Renesansa", "Rafael");
                mojaBazaVar.insertPeriod("Barok", "Michelangelo Merisi da Caravaggio");
                mojaBazaVar.insertPeriod("Realizam", "Gustave Courbet");
                mojaBazaVar.insertPeriod("Impresionizam", "Claude Monet");
                mojaBazaVar.insertPeriod("Nadrealizam", "Salvador Dali");

                mojaBazaVar.close();
            }
        });

        Button ispis = findViewById(R.id.ispis);
        ispis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView tekst = findViewById(R.id.ispisTekst);

                mojaBazaVar.open();

                Cursor cSlike = mojaBazaVar.getAllSlike();

                tekst.setText("Slike:\n");

                if(cSlike.moveToFirst()){

                    do{
                        tekst.append("id: " + cSlike.getInt(0) + " " + cSlike.getString(1) + ", " + cSlike.getString(2) + "\n");
                    }while (cSlike.moveToNext());

                }


                Cursor cPeriodi = mojaBazaVar.getAllPeriod();
                tekst.append("Periodi:\n");

                if(cPeriodi.moveToFirst()){

                    do{
                        tekst.append("id: " + cPeriodi.getInt(0) + " " + cPeriodi.getString(1) + ", " + cPeriodi.getString(2) + "\n");
                    }while (cPeriodi.moveToNext());

                }

                mojaBazaVar.close();

            }
        });

        Button ispisPicassa = findViewById(R.id.ispisPicassa);
        ispisPicassa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView tekst = findViewById(R.id.ispisTekst);

                mojaBazaVar.open();

                Cursor cPicasso = mojaBazaVar.getSlikaByAutor("Picasso");

                tekst.setText("Picasso:\n");

                if(cPicasso.moveToFirst()){

                    do{
                        tekst.append("id: " + cPicasso.getInt(0) + " " + cPicasso.getString(1) + ", " + cPicasso.getString(2) + "\n");
                    }while (cPicasso.moveToNext());

                }

                mojaBazaVar.close();

            }
        });

        //primjer brisanja
        /*
        mojaBazaVar.open();
        mojaBazaVar.deleteSlika(1);
        mojaBazaVar.deletePeriod(1);
        mojaBazaVar.close();
        */
    }
}
