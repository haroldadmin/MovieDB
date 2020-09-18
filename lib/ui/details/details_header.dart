import 'package:flutter/material.dart';
import 'package:moviedb/ui/home/movies_grid.dart';

class DetailsHeader extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Stack(
          alignment: Alignment.bottomLeft,
          clipBehavior: Clip.none,
          children: [
            _DetailsHeaderBackground(),
            _DetailsHeaderForeground(),
          ],
        ),
      ],
    );
  }
}

class _DetailsHeaderBackground extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      height: 200,
      width: double.infinity,
      color: colors[1],
    );
  }
}

class _DetailsHeaderForeground extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      transform: Matrix4.translationValues(0.0, 20.0, 0.0),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          _Poster(),
          _MovieControls(),
        ],
      ),
    );
  }
}

class _Poster extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      color: colors[4],
      height: 160,
      width: 120,
      margin: const EdgeInsets.only(left: 16.0, right: 16.0),
    );
  }
}

class _MovieControls extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 16.0),
        child: Row(
          children: [
            Expanded(
              child: RaisedButton(
                child: Text("Favourite"),
                onPressed: () {},
                color: Colors.red,
              ),
            ),
            SizedBox(width: 8.0),
            Expanded(
              child: RaisedButton(
                child: Text("Watchlist"),
                onPressed: () {},
                color: Colors.red,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
