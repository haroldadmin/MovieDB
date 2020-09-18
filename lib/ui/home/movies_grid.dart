import 'package:flutter/material.dart';

typedef OnMovieClick = void Function();

class MoviesGrid extends StatelessWidget {
  final GestureTapCallback _onClicked;

  MoviesGrid(GestureTapCallback onClicked) : _onClicked = onClicked;

  @override
  Widget build(BuildContext context) {
    return GridView.builder(
      gridDelegate:
          SliverGridDelegateWithFixedCrossAxisCount(crossAxisCount: 3),
      padding: EdgeInsets.only(top: 100.0),
      itemCount: 100,
      itemBuilder: (context, index) => InkWell(
        onTap: _onClicked,
        child: Container(
          height: 150.0,
          color: colors[index % colors.length],
        ),
      ),
    );
  }
}

const colors = <Color>[
  Colors.red,
  Colors.blue,
  Colors.green,
  Colors.indigo,
  Colors.teal,
  Colors.amber,
  Colors.blueGrey,
  Colors.orange,
];
