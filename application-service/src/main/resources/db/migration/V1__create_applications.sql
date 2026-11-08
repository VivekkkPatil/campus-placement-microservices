CREATE TABLE applications (
  id BIGSERIAL PRIMARY KEY,
  student_id BIGINT NOT NULL,
  drive_id BIGINT NOT NULL,
  status VARCHAR(20) NOT NULL,
  version BIGINT NOT NULL DEFAULT 0,
  created_at TIMESTAMPTZ,
  updated_at TIMESTAMPTZ,
  CONSTRAINT uq_student_drive UNIQUE (student_id, drive_id)
);

CREATE TABLE application_events (
  id BIGSERIAL PRIMARY KEY,
  application_id BIGINT NOT NULL REFERENCES applications(id),
  from_status VARCHAR(20),
  to_status VARCHAR(20) NOT NULL,
  changed_at TIMESTAMPTZ NOT NULL
);
