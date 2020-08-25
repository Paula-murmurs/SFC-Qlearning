import java.util.HashMap;
import java.util.Map;

public class VnfManager {
	public  Map vnfs = new HashMap();

    public Map getVnfs() {
        return vnfs;
    }

    public void setVnfs(Map vnfs) {
        this.vnfs = vnfs;
    }
}
