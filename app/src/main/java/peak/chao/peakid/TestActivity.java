package peak.chao.peakid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import peak.chao.annotation.Id;
import peak.chao.id.BindIdClass;

public class TestActivity extends AppCompatActivity {
    @Id(R.id.tv)
    TextView tv;
    @Id(R.id.tv)
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindIdClass.bind(this);
        tv.setText("测试");
        //new XXTest();
    }
}
