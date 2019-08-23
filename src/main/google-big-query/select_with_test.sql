SELECT * FROM `type-cast-repair.npe_bugs.npe_bugs_2014` WHERE (payload LIKE '%npe%' OR payload LIKE '%NullPointerException%')
AND payload LIKE '%test%'