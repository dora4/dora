package dora.widget.panel;

/**
 * 枝和叶的顶级接口。
 */
public interface MenuPanelItemRoot {

    boolean hasTitle();

    /**
     * 菜单的标题。
     *
     * @return
     */
    String getTitle();

    void setTitle(String title);

    /**
     * 获取标题四周的间距。
     *
     * @return
     */
    Span getTitleSpan();

    void setTitleSpan(Span titleSpan);

    /**
     * 菜单的上边距。
     *
     * @return
     */
    int getMarginTop();

    void setMarginTop(int marginTop);

    class Span {
        int left;
        int top;
        int right;
        int bottom;

        public Span() {
        }

        /**
         * 根据水平间距和垂直间距设置四周的间距，常用。
         *
         * @param horizontal
         * @param vertical
         */
        public Span(int horizontal, int vertical) {
            this(horizontal, vertical, horizontal, vertical);
        }

        public Span(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public int getLeft() {
            return left;
        }

        public int getTop() {
            return top;
        }

        public int getRight() {
            return right;
        }

        public int getBottom() {
            return bottom;
        }
    }
}
