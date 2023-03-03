package com.nrifintech.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nrifintech.exception.ResourceNotFoundException;
import com.nrifintech.model.Author;
import com.nrifintech.model.Book;
import com.nrifintech.model.Genre;
import com.nrifintech.repository.BookRepository;

@Service
public class BookService
{
	@Autowired
	private BookRepository bookrepo;
	
	@Autowired
	private AuthorService as;
	
	@Autowired
	private GenreService gs;
	
	public ResponseEntity<Book> addBook(Book book)
	{
		Author a=book.getAuthor();
		Genre g=book.getGenre();
		a.setAuthorId(as.getAuthorId(a.getAuthorName()));
		g.setGenreId(gs.getGenreId(g.getGenreName()));
		book.setAuthor(a);
		book.setGenre(g);
		bookrepo.save(book);
		return ResponseEntity.ok().body(book);
	}
	
	public ResponseEntity<Book> updateBook(Integer bookId,Book bnew) throws ResourceNotFoundException
	{

		Book b=bookrepo.findById(bookId).orElseThrow(()->new ResourceNotFoundException("Book not found for this id "+bookId));
		Author a=new Author();
		Genre g=new Genre();
		a.setAuthorId(as.getAuthorId(bnew.getAuthor().getAuthorName()));
		a.setAuthorName(as.getAuthorname(a.getAuthorId()));
		g.setGenreId(gs.getGenreId(bnew.getGenre().getGenreName()));
		g.setGenreName(gs.getGenrename(g.getGenreId()));
		b.setAuthor(a);
		b.setGenre(g);
		b.setDate(bnew.getDate());
		b.setQty(bnew.getQty());
		b.setTitle(bnew.getTitle());
		b.setUrl(bnew.getUrl());
		b.setISBN(bnew.getISBN());
		b.setDescription(bnew.getDescription());
		bookrepo.save(b);
		return ResponseEntity.ok().body(b);
	}
	
	public ResponseEntity<Book> deleteBook(Integer bookId) throws ResourceNotFoundException
	{
		Book book=bookrepo.findById(bookId).orElseThrow(()->new ResourceNotFoundException("Book not found for this id "+bookId));
		bookrepo.delete(book);
		return ResponseEntity.ok().body(book);
	}
	
	public List<Book> getAllBooks()
	{
		List<Book> bl=new ArrayList<Book>();
		for(Book b:bookrepo.findAll())
		{
			bl.add(b);
		}
		return bl;
	}
	
	public ResponseEntity<Book> getBookById(int bookId) throws ResourceNotFoundException
	{
		Book book=bookrepo.findById(bookId).orElseThrow(()->new ResourceNotFoundException("Book not found for this id "+bookId));
		return ResponseEntity.ok().body(book);
	}
	
	public ResponseEntity<Book> getBookByTitle(String title)
	{
		for(Book b:bookrepo.findAll())
		{
			if(b.getTitle().equalsIgnoreCase(title))
			{
				 ResponseEntity.ok().body(b);
			}
		}
		return ResponseEntity.ok().body(null);
	}
	
	public ResponseEntity<List<Book>> getBookByGenre(String genreName)
	{
		List<Book> bl=new ArrayList<Book>();
		for(Book b:bookrepo.findAll())
		{
			if(b.getGenre().getGenreName().equalsIgnoreCase(genreName))
			{
				bl.add(b);
			}
		}
		return ResponseEntity.ok().body(bl);
	}
	
	public ResponseEntity<List<Book>> getBookByAuthor(String authorName)
	{
		List<Book> bl=new ArrayList<Book>();
		for(Book b:bookrepo.findAll())
		{
			if(b.getAuthor().getAuthorName().equalsIgnoreCase(authorName))
			{
				bl.add(b);
			}
		}
		return ResponseEntity.ok().body(bl);
	}
	
	public ResponseEntity<Book> getBookByIsbn(long isbn) throws ResourceNotFoundException
	{
		for(Book book:bookrepo.findAll())
		{
			if(book.getISBN()==isbn)
			{

				return ResponseEntity.ok().body(book);
			}
		}
		return ResponseEntity.ok().body(null);
	}
	
	public ResponseEntity<List<Book>> getAvailableBooks()
	{
		List<Book> availableBooks=new ArrayList<Book>();
		for(Book b:bookrepo.findAll())
		{
			if(b.getQty()>0)
			{
				availableBooks.add(b);
			}
		}
		return ResponseEntity.ok().body(availableBooks);
	}
	
	public ByteArrayOutputStream generateReport()
	{
		XSSFWorkbook workbook=new XSSFWorkbook();
		XSSFSheet sheet=workbook.createSheet("Books");
		ByteArrayOutputStream bs=new ByteArrayOutputStream();
		int rownum=1;
		Row rownames=sheet.createRow(rownum++);
		int cellnum=1;
		Cell cellidName=rownames.createCell(cellnum++);
		cellidName.setCellValue("bookId");
		Cell celltitleName=rownames.createCell(cellnum++);
		celltitleName.setCellValue("Title");
		Cell cellIsbn=rownames.createCell(cellnum++);
		cellIsbn.setCellValue("ISBN");
		Cell cellquantityName=rownames.createCell(cellnum++);
		cellquantityName.setCellValue("Quantity");
		Cell celldateName=rownames.createCell(cellnum++);
		celldateName.setCellValue("Date of added");
		Cell cellauthorName=rownames.createCell(cellnum++);
		cellauthorName.setCellValue("Author Name");
		Cell cellgenreName=rownames.createCell(cellnum++);
		cellgenreName.setCellValue("Genre Name");
		Cell cellImageLink=rownames.createCell(cellnum++);
		cellImageLink.setCellValue("Image Link");
		Cell cellDescription=rownames.createCell(cellnum++);
		cellDescription.setCellValue("Description");
		for(Book b:bookrepo.findAll())
		{
			Row rowvalues=sheet.createRow(rownum++);
			cellnum=1;
			Cell cellid=rowvalues.createCell(cellnum++);
			cellid.setCellValue(b.getBookId());
			Cell celltitle=rowvalues.createCell(cellnum++);
			celltitle.setCellValue(b.getTitle());
			Cell cellIsbnvalue=rownames.createCell(cellnum++);
			cellIsbnvalue.setCellValue(b.getISBN());
			Cell cellquantity=rowvalues.createCell(cellnum++);
			cellquantity.setCellValue(b.getQty());
			Cell celldate=rowvalues.createCell(cellnum++);
			celldate.setCellValue(b.getDate());
			Cell cellauthor=rowvalues.createCell(cellnum++);
			cellauthor.setCellValue(b.getAuthor().getAuthorName());
			Cell cellgenre=rowvalues.createCell(cellnum++);
			cellgenre.setCellValue(b.getGenre().getGenreName());
			Cell cellImage=rownames.createCell(cellnum++);
			cellImage.setCellValue(b.getUrl());
			Cell cellDescriptionvalue=rownames.createCell(cellnum++);
			cellDescriptionvalue.setCellValue(b.getDescription());
		}
		try
		{
			workbook.write(bs);
			bs.close();
			workbook.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

		return bs;
	}
}
