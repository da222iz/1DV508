package resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import movies.Movie;

@Named
@SessionScoped
public class Cart implements Serializable{
	List<Movie> contents = new ArrayList<>();
	
	public List<Movie> getContents() {
		return contents;
	}
	public void setContents(List<Movie> contents) {
		this.contents = contents;
	}
	public void AddToCart(Movie m){
		Movie tempMovie=new Movie();
		tempMovie.setId(m.getId());
		tempMovie.setTitle(m.getTitle());
		tempMovie.setDescription(m.getDescription());
		tempMovie.setGenre(m.getGenre());
		tempMovie.setPrice(m.getPrice());
		tempMovie.setQuantity(1);
		if (this.contains(tempMovie)){
			int index=this.getIndexOfMovie(tempMovie);
			int currentQuantity=contents.get(index).getQuantity();
			contents.get(index).setQuantity(currentQuantity+1);
		}
		else{
			contents.add(tempMovie);
		}
		
	}
	public String delete(Movie x){
		int index=this.getIndexOfMovie(x);
		if (index>-1)
			contents.remove(index);
		return "my_cart.xhtml";
	}
	public boolean contains(Movie m){
		boolean result=false;
		for (int i=0; i<contents.size(); i++){
			if(m.getTitle().equals(contents.get(i).getTitle())){
				result=true;
				break;
			}	
		}
		return result;
	}
	public int getIndexOfMovie(Movie m){
		int index=-1;
		for (int i=0; i<contents.size(); i++){
			if(m.getTitle().equals(contents.get(i).getTitle())){
				index=i;
				break;
			}
		}
		return index;
	}
	

}
