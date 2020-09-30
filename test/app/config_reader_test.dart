import 'package:flutter_test/flutter_test.dart';
import 'package:moviedb/app/config_reader.dart';

void main() {
  test("ConfigReader parses the correct config", () {
    final reader = ConfigReader();
    final configString = """{
      "api_key": "mock-api-key"
    }""";

    final config = reader.readFrom(configString);
    expect(config.apiKey, isNotEmpty);
    expect(config.apiKey, "mock-api-key");
  });
}
