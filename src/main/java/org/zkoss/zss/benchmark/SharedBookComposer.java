package org.zkoss.zss.benchmark;


import java.io.IOException;
import java.util.*;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.model.*;
import org.zkoss.zss.ui.Spreadsheet;

public class SharedBookComposer extends SelectorComposer<Component> {
    private static final long serialVersionUID = 1L;
    @Wire
    protected Spreadsheet ss;
    
    static private final Map<String,Book> sharedBook = new HashMap<String,Book>();
    String BOOK_NAME = "normal.xlsx";
 
    public void doAfterCompose (Component comp) throws Exception {
    	super.doAfterCompose(comp);
        Book book = loadBookFromAvailable(BOOK_NAME);
        ss.setBook(book);
    }
     
    private Book loadBookFromAvailable(String bookname){
        Book book;
        synchronized (sharedBook){
            book = sharedBook.get(bookname);
            if(book==null){
                book = importBook(bookname);
                book.setShareScope("application");
                sharedBook.put(bookname, book);
            }
        }
        return book;
    }
     
    private Book importBook(String bookname){
    	Importer imp = Importers.getImporter("excel");
    	Book book = imp.imports(
    			WebApps.getCurrent().getResourceAsStream("/" + bookname),
    			bookname);
    	return book;
    }
}