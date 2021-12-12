package adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.game_1.R;
import java.util.List;
import models.Utils;
import models.OnItemClickListener;
import models.Score;

public class ScoreTableAda extends RecyclerView.Adapter<ScoreTableAda.ViewHolder> {

    public ScoreTableAda(List<Score> score, OnItemClickListener listener) {
        this.scores = score;
        this.listener = listener;
    }
    private List<Score> scores;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View score_item = LayoutInflater.from(parent.getContext()).inflate(R.layout.score,parent,false);
        return new ViewHolder(score_item);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Score cur = scores.get(position);
        holder.placeTv.setText("" + (position+1));
        holder.scoreTv.setText(Utils.getTimeString(cur.getTimeLasted()));
        holder.itemView.setOnClickListener(v -> {
            listener.Clicked(cur);
        });
    }
    @Override
    public int getItemCount() {
        return scores.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView placeTv,scoreTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeTv = itemView.findViewById(R.id.scorePlace);
            scoreTv = itemView.findViewById(R.id.scoreItem);
        }
    }
}
