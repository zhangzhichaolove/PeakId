package peak.chao.peakid;

import peak.chao.id.BindId;

public class MainActivityId implements BindId<MainActivity> {
    @Override
    public void bind(MainActivity target) {
        target.tv = target.findViewById(R.id.tv);
    }

    @Override
    public void unBind() {

    }
}
