#!/usr/bin/perl

use strict;
use DBI;

my $db = shift or die "needs a database file.";
$db =~ s/\\/\//g;

my $dbh = DBI->connect(          
    #~ "dbi:SQLite:dbname=test.db", 
    "dbi:SQLite:dbname=$db", 
    #~ "dbi:SQLite:dbname=:memory:",
    "",                          
    "",                          
    { RaiseError => 1 },         
) or die $DBI::errstr;

my $sth = $dbh->prepare("select * from fileindex where hash in (select hash from FileIndex group by hash having count(*) > 1) order by hash");
$sth->execute();

my $row;
while ($row = $sth->fetchrow_arrayref()) {
    print "@$row[0] @$row[3] \n";
}

$dbh->disconnect();
