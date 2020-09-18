import 'package:flutter/material.dart';
import 'package:moviedb/ui/details/details_header.dart';

class DetailsPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Details"),
      ),
      body: Column(
        mainAxisSize: MainAxisSize.max,
        children: <Widget>[
          DetailsHeader(),
          _MovieTitle(),
        ],
      ),
    );
  }
}

class _MovieTitle extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Text(
      "Awesome Movie",
      style: Theme.of(context).textTheme.headline1,
    );
  }
}
