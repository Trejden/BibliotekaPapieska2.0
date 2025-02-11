package Common;

public class BookDto {
    public int Id;
    public String IBAN;
    public String Title;
    public String Author;
    public BookDto(int id, String iban, String title, String author){
        this.Id = id;
        this.IBAN = iban;
        this.Title = title;
        this.Author = author;
    }
}
