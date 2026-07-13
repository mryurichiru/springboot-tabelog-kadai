package com.example.service;

import org.springframework.stereotype.Service;

import com.example.repository.FavoriteRepository;

@Service
public class FavoriteService {
private final FavoriteRepository favoriteRepository;

public FavoriteService(FavoriteRepository favoriteRepository) {
	this.favoriteRepository = favoriteRepository;
	}
}
