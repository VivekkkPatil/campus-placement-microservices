CREATE TABLE companies (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(100) UNIQUE NOT NULL,
  industry VARCHAR(60),
  website VARCHAR(150),
  created_at TIMESTAMPTZ,
  updated_at TIMESTAMPTZ
);

CREATE TABLE drives (
  id BIGSERIAL PRIMARY KEY,
  company_id BIGINT NOT NULL REFERENCES companies(id),
  role VARCHAR(100) NOT NULL,
  package_lpa NUMERIC(5,2) NOT NULL,
  min_cgpa NUMERIC(4,2) NOT NULL,
  max_backlogs INTEGER NOT NULL DEFAULT 0,
  allowed_branches VARCHAR(200) NOT NULL,
  graduation_year INTEGER NOT NULL,
  deadline DATE NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
  version BIGINT NOT NULL DEFAULT 0,
  created_at TIMESTAMPTZ,
  updated_at TIMESTAMPTZ
);
