
use strict;
use warnings;
use feature 'say';

use Cwd 'abs_path';
use File::Find;
use Digest::MD5 qw(md5_hex);

my $dir = shift;
my $index = shift;
(defined $dir and defined $index) or die "Usage: $0 <dir> <indexfile>\n";

my $count = 0;

open IDX, ">", "$index" or die $!;

find( { wanted => \&index_files, 
		no_chdir => 1,
	}, 
	abs_path("$dir") );
	

say "$count files indexed";

sub index_files {
	if (-d){
		# chdir manuel, le chdir automatique de File::Find se place dans le parent
		# moi j'ai besoin d'être dans le dossier 
		chdir $_;

		# si on tombe sur un répertoire svn on quitte
		# prune permet d'ignorer tout les sous répertoires		
		if($_ =~ /\.svn$/){
			$File::Find::prune = 1;
			return;
		}
	} elsif (-f) {
		$count++;
		open my $inp, $_ or die $!;
		binmode($inp);
		say IDX Digest::MD5->new->addfile($inp)->hexdigest, "\t$_";
		close $inp;
	}
}

close IDX;
