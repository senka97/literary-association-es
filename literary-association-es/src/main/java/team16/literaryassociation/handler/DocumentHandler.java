package team16.literaryassociation.handler;

import team16.literaryassociation.model.es.BookES;

import java.io.File;

public abstract class DocumentHandler {
	/**
	 * Od prosledjene datoteke se konstruise Lucene Document
	 * 
	 * @param file datoteka u kojoj se nalaze informacije
	 * @return Lucene Document
	 */
	public abstract BookES getIndexUnit(File file);

	public abstract String getText(File file);

}
