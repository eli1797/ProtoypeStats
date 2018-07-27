package eli.protoypestats;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import eli.protoypestats.dummy.DummyContent;

public class List extends AppCompatActivity implements PlayerFragment.OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);




    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item){
    }


}
