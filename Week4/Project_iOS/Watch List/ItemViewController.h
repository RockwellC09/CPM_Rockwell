//
//  ItemViewController.h
//  Watch List
//
//  Created by Christopher Rockwell on 6/12/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>
#import "Reachability.h"

@interface ItemViewController : UIViewController
{
    IBOutlet UILabel *titleLabel;
    IBOutlet UILabel *showing;
    NSMutableArray *titlesArray;
    NSMutableArray *timesArray;
    NSMutableArray *daysArray;
    NSMutableArray *idsArray;
    int count;
}

-(IBAction)onClick:(id)sender;
@end
