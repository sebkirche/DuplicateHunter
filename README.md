Duplicate Hunter
===============

Duplicate Hunter is a java tool to help for searching duplicated files. It was designed to search for local files that were also present in a SVN repository.

The principle is that you index some files by their MD5 hash and then you can search index duplicates.

At first it was a command-line only tool that could produce indexes for the console or text files to parse with Perl. It evolved and now has a minimal GUI interface and stores the index into a SQLite database.

![gui prototype](/docs/dh_gui.png "gui prototype")

For now, only local filesystems and SVN repositories (via SvnKit) can be indexed. I am planning to add at least Git as another scm if I find a Java library for it.

```
usage: java -jar DuplicateHunter.jar <options>
options:
 -d,--db <arg>        name of the database to save index
 -f,--folder <arg>    path to a directory to index
    --gui             Use experimental graphical user interface
 -h,--help            show command line usage
 -i,--index           produce an index
 -o <arg>             name of the file to save index
 -s,--svnrepo <arg>   URL of the repository (svn:// http:// or file://)

```
