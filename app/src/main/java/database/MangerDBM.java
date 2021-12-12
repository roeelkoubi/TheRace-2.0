package database;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import models.OnScoreResponse;
import models.Score;

public class MangerDBM {
    private final DatabaseReference root;
    private static final MangerDBM shared = new MangerDBM();
    public static MangerDBM getInstance() {
        return shared;
    }
    private MangerDBM() {
        root = FirebaseDatabase.getInstance("https://spongebobrace-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    }
    public void addNewScore(Score score, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        root.child("scores").push().setValue(score)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }
    public void fetchScores(OnScoreResponse listener) {
        root.child("scores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Score> scores = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child == null)
                        continue;
                    Score score = child.getValue(Score.class);
                    scores.add(score);
                }
                listener.consumeScores(scores);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.print(error.getDetails());
            }
        });
    }
}
