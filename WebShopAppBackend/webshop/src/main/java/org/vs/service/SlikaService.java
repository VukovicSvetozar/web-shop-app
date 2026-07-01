package org.vs.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SlikaService {

	private final Path root = Paths.get("sacuvaneSlike");

	public void init() {
		try {
			Files.createDirectories(root);
		} catch (IOException e) {
			throw new RuntimeException("Ne moze se kreirati pocetni direktorijum!");
		}
	}

	public void sacuvaj(MultipartFile file) {
		try {
			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			if (e instanceof FileAlreadyExistsException) {
				throw new RuntimeException("Ime slike je zauzeto.");
			}
			throw new RuntimeException(e.getMessage());
		}
	}

	public Stream<Path> vratiSveSlike() {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Ne mogu ucitati datoteke!");
		}
	}

	public Stream<Path> vratiSlikeProizvoda(Integer proizvodId) {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize)
					.filter(path -> {
						try {
							int cifraProizvoda = Integer.parseInt(path.toString().split("_")[0]);
							return cifraProizvoda == proizvodId;
						} catch (NumberFormatException e) {
							return false;
						}
					});
		} catch (IOException e) {
			throw new RuntimeException("Ne mogu ucitati datoteke!");
		}
	}

	public Stream<Path> vratiPoster(Integer proizvodId) {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize)
					.filter(path -> {
						try {
							int cifraProizvoda = Integer.parseInt(path.toString().split("_")[0]);
							int cifraPostera = Integer.parseInt(path.toString().split("_")[1]);
							return cifraProizvoda == proizvodId && cifraPostera == 1;
						} catch (NumberFormatException e) {
							return false;
						}
					});
		} catch (IOException e) {
			throw new RuntimeException("Ne mogu ucitati datoteke!");
		}
	}

	public Resource vratiSliku(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Ne mogu ucitati datoteku!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Greska: " + e.getMessage());
		}
	}

}
