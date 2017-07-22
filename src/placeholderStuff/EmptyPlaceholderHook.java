package placeholderStuff;

public class EmptyPlaceholderHook extends PlaceholderSupport {

    @Override
    public void setPlaceHolder(String variable, Object value) {
        //Leave it blank, since it doesn't have the placeholder, it will take no actions
    }
    @Override
    public Object getPlaceHolder(String variable) {
        return null;
    }
}
