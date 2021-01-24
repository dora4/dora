package dora.util;

import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public final class ViewUtils {

    private ViewUtils() {
    }

    /**
     * 默认配置。
     *
     * @param recyclerView
     */
    public static RecyclerView configRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
        return recyclerView;
    }

    /**
     * 自定义配置。
     *
     * @param recyclerView
     * @param layoutManager
     */
    public static RecyclerView configRecyclerView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return recyclerView;
    }

    public static String getText(TextView textView) {
        return textView.getText().toString().trim();
    }
}
