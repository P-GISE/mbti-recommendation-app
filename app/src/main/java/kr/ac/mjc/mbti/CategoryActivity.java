package kr.ac.mjc.mbti;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.cardview.widget.CardView;

public class CategoryActivity extends AppCompatActivity {

    private CardView btnBaseball, btnGame, btnMovie, btnTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_category);

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // 뷰 초기화
            initViews();

            // 클릭 리스너 설정
            setupClickListeners();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "앱 초기화 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 뷰 초기화
     */
    private void initViews() {
        try {
            btnBaseball = findViewById(R.id.btn_baseball);
            btnGame = findViewById(R.id.btn_game);
            btnMovie = findViewById(R.id.btn_movie);
            btnTravel = findViewById(R.id.btn_travel);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "버튼을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 클릭 리스너 설정
     */
    private void setupClickListeners() {
        if (btnBaseball != null) {
            btnBaseball.setOnClickListener(v -> {
                startQuestionActivity("야구구단");
            });
        }

        if (btnGame != null) {
            btnGame.setOnClickListener(v -> {
                startQuestionActivity("게임포지션");
            });
        }

        if (btnMovie != null) {
            btnMovie.setOnClickListener(v -> {
                startQuestionActivity("영화장르");
            });
        }

        if (btnTravel != null) {
            btnTravel.setOnClickListener(v -> {
                startQuestionActivity("여행지");
            });
        }
    }

    /**
     * 질문 화면으로 이동
     */
    private void startQuestionActivity(String category) {
        try {
            Intent intent = new Intent(CategoryActivity.this, QuestionActivity.class);
            intent.putExtra("category", category);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "화면 전환 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}