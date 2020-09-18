import 'package:flutter/material.dart';
import 'package:moviedb/ui/details/details.dart';
import 'package:moviedb/ui/home/movies_grid.dart';
import 'package:moviedb/ui/home/searchbar.dart';

class HomePage extends StatelessWidget {
  void _onMovieClicked(BuildContext context) {
    Navigator.of(context)
        .push(MaterialPageRoute(builder: (context) => DetailsPage()));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("MovieDB")),
      body: Stack(
        children: <Widget>[
          MoviesGrid(() {
            _onMovieClicked(context);
          }),
          Searchbar(),
        ],
      ),
    );
  }
}
