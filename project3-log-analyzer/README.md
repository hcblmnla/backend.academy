### Useful information

Usage instructions below

```bash
java -jar analyzer.jar -p=<path> [-f=<from>] [-t=<to>] [-o=<format>] [--file=<file>]
                        [filter-user=<userFilter>] [filter-request=<requestFilter>]  

  -p, --path=<path>     path to NGINX log files
  -f, --from=<from>     from what time
  -t, --to=<to>         before what time

  -o, --out=<format>    output format
      --file=<file>     output filename

      --filter-user=<userFilter>        users filter
      --filter-request=<requestFilter>  requests filter
```

Rendered table example (markdown) you can find [here](reports/example.md)

### Known issues:

* when starting from `bash` file pattern should be highlighted in `''` like a

```bash
java -jar analyzer.jar -p src/'**'
```
