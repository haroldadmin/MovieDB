import 'package:flutter/material.dart';
import 'package:moviedb/ui/home/movies_grid.dart';
import 'package:moviedb/ui/home/searchbar.dart';

class HomePage extends StatelessWidget {
  void _onMovieClicked() {
    print("Clicked!");
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("MovieDB")),
      body: Stack(
        children: <Widget>[MoviesGrid(_onMovieClicked), Searchbar()],
      ),
    );
  }
}
