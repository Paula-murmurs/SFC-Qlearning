import java.util.List;

public class ServiceChainRequest {
	public List sfc ;

    public ServiceChainRequest(List<Character> sfc) {
        this.sfc = sfc;
    }

	public List getSfc() {
        return sfc;
    }
}
