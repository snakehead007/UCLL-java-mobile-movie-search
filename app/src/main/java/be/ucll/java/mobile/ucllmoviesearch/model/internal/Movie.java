package be.ucll.java.mobile.ucllmoviesearch.model.internal;

/**
 * Custom Movie Pojo storing what we find useful from the returned REST JSON Webservice
 */
public class Movie implements Comparable {
    private String title;
    private String year;
    private short yearNr;
    private String posterURL;
    private boolean favorite = false;
    private String imdbID;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
        try {
            yearNr = Short.valueOf(year);
        } catch (NumberFormatException e) {
            yearNr = 0;
        }
    }

    public short getYearNr() {
        return yearNr;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public void toggleFavorite() {
        favorite = !favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Movie) {
            Movie m = (Movie) o;
            if (isFavorite() && !m.isFavorite()) {
                return -1;
            } else if (!isFavorite() && m.isFavorite()) {
                return 1;
            } else {
                return Short.compare(m.getYearNr(), yearNr);
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", yearNr=" + yearNr +
                ", posterURL='" + posterURL + '\'' +
                ", favorite=" + favorite +
                ", imdbID='" + imdbID + '\'' +
                '}';
    }
}