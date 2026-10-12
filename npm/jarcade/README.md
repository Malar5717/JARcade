# jarcade

Java-free terminal arcade — **Tic Tac Toe**, **Wordle** and **Hangman** in your
terminal. Ships as a single self-contained native binary, so **no Java runtime is
required**.

## Usage

Run it without installing:

```sh
npx jarcade
```

Or install it globally and use the `jarcade` command:

```sh
npm install -g jarcade
jarcade
```

## Supported platforms

| Platform              | Package                 |
| --------------------- | ----------------------- |
| Windows x64           | `jarcade-win32-x64`     |
| Linux x64             | `jarcade-linux-x64`     |
| macOS (Apple Silicon) | `jarcade-darwin-arm64`  |

npm installs only the package that matches your OS and CPU; the others are
skipped automatically.

## How it works

This package is a tiny Node.js launcher. It detects your platform and runs the
matching prebuilt binary from a platform-specific package. The binaries are
produced with [GraalVM Native Image](https://www.graalvm.org/) from the sources
at <https://github.com/Malar5717/JARcade>.

## Source

<https://github.com/Malar5717/JARcade>
