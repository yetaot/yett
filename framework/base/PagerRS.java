package framework.base;

import java.util.List;

public class PagerRS<T> {
	
	private Pager pager;
	
	private List<T> results;
	
	public PagerRS(List<T> results, Pager pager) {
		this.results = results;
		this.pager = pager;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}
	
	
	

}
