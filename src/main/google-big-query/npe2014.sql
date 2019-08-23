SELECT * FROM ([githubarchive:year.2014]) WHERE type='PushEvent' AND other LIKE '%"language":"Java"%' 
AND other NOT LIKE '%ndroid%'
AND other NOT LIKE '%"stargazers":[0-9],%' 
AND other NOT LIKE '%"size":[0-9],%'
AND other NOT LIKE '%"size":[1-9][0-9],%'
AND other NOT LIKE '%"size":[1-9][0-9][0-9],%'
AND other NOT LIKE '%"size":[1-9][0-9][0-9][0-9],%'
AND (payload LIKE '%NPE%'OR payload LIKE '%Null Pointer Exception%')
AND payload LIKE '%fix%'