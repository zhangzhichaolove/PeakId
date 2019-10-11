package peak.chao.id;

public interface BindId<T> {

    void bind(T target);

    void unBind();
}
