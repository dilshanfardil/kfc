package lk.sayuru.jungleapp;

import androidx.appcompat.app.AppCompatActivity;
import lk.sayuru.jungleapp.content.PlaceContent;
import lk.sayuru.jungleapp.path.PlaceFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseActivity extends AppCompatActivity implements PlaceFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        Button button=findViewById(R.id.btnAddPath);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.pathPointRepository.deleteAllViewPoints();
                startActivity(new Intent(ChooseActivity.this,MapActivity.class));
            }
        });

        Button btnViewPath=findViewById(R.id.btnViewPath);
        btnViewPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.pathPointRepository.deleteAllDataAddedPoints();
                startActivity(new Intent(ChooseActivity.this,SelectPathActivity.class));
            }
        });
    }

    @Override
    public void onListFragmentInteraction(PlaceContent.PlaceItem item) {

    }
}
