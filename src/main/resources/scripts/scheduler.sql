DELIMITER $$

CREATE EVENT update_endorsement_status
ON SCHEDULE EVERY 1 DAY
STARTS TIMESTAMP(CURRENT_DATE, '00:00:00') -- Runs daily at midnight
DO
BEGIN
    -- Update Authority Goals where the endorsement deadline has passed
    UPDATE authoritygoals ag
    JOIN yearlygoalssettings ygs ON ag.year = ygs.year
    SET ag.status = 'EndorsementCompleted'
    WHERE ygs.endorsement_deadline IS NOT NULL 
    AND ygs.endorsement_deadline < CURRENT_TIMESTAMP;

    -- Update Department Goals where the corresponding authority goal is updated
    UPDATE departmentgoals dg
    JOIN authoritygoals ag ON dg.authgoalid = ag.id
    SET dg.status = 'Approved'
    WHERE ag.status = 'EndorsementCompleted'
    AND dg.chosenbydefault = 1;
    
END $$

DELIMITER ;
