package ysaak.anima.data;


public class AnimeEpisode {

    private String number;

    private String title;

    public AnimeEpisode() {
    }

    public AnimeEpisode(String number, String title) {
        this.number = number;
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
