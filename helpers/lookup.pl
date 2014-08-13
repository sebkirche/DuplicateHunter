
use strict;
use warnings;
use feature 'say';
use Data::Dumper;

my $svnindex = shift // die "need a svn index file";
my $dirindex = shift;
my %svn;

die "$svnindex does not exist ? $!" if(!-f $svnindex);

open SVN, '<:encoding(utf-8)', $svnindex or die $!;
my $hashval;
my $file;
while(<SVN>){
	if(/(.*)\t(.+)/) {
		$hashval = $1;
		$file = $2;
		#~ say $hashval, $file;
		if(exists $svn{$hashval}){
			#~ say "duplicate: $file <=> $svn{$hashval}[0]";
			push $svn{$hashval}, $file;
		} else {
			#~ $svn{$hashval} = [ $file ];
			$svn{$hashval} = [ $file ];
		}
	}
}
close SVN;

foreach my $h (keys %svn) {
	if (scalar $svn{$h} > 1){
		say $h;
		#~ say ref $svn{$h} ;
		#~ say Dumper();
		#~ say scalar ($svn{$h});
		my $t = $svn{$h};
		#~ foreach my $v ( $svn{$h} ){
		foreach my $v ( @$t ){
			say $v;
		}
		#~ exit 0;
	}
}
