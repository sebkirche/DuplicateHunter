create table if not exists FileIndex (
	path   text primary key not null,
	repo   text             not null,
	name   text             not null,
	hash   text             not null,
	lastup datetime         not null,
	author text,
	size   int              not null
);
create index if not exists repo_idx on FileIndex (repo);

create table if not exists Sources (
	path text unique on conflict replace,
	active boolean default false
);
